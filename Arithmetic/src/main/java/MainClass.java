import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class MainClass {
    public static final int NUMBER_OF_OPERATOR = 3;
    public static final int PLUS = 1;
    public static final int MINUS = 2;
    public static final int MULTIPLE = 3;
    public static final int DEVICE = 4;
    public static final int PlusAndMinTypeMark = 1; // 表明加减运算符的类型
    public static final int MulAndDevTypeMark = 2; // 表明乘除运算符的类型


    public static void main(String[] args) {
        // 接受命令行输入
        long rawTimestamp = System.currentTimeMillis();
        int num = 0;
        int range = 0;
        String exerciseFilePath = null;
        String answerFilePath = null;
        label:
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-n":
                    // 此处如果收到了1.1，自动将其转型为1
                    if (i + 1 < args.length) {
                        if (Util.judgeNotAllNumber(args[i + 1])) {
                            // 未指定输入
                            ArgumentsException NException = new ArgumentsException("请输入-n后的值！");
                            NException.printStackTrace();//打印异常栈追踪信息
                            System.exit(1); // 退出程序
                        }

                        // 此处如果收到了1.1，自动将其转型为1
                        num = Integer.parseInt(args[i + 1]);
                        if (Util.checkArgument(num)) {
                            // 未指定输入
                            ArgumentsException RException = new ArgumentsException("请保证-n后的值为自然数！");
                            RException.printStackTrace();//打印异常栈追踪信息
                            System.exit(1); // 退出程序
                        }
                    } else {
                        // 如果 -r 后没有元素，则创建异常对象
                        ArgumentsException RException = new ArgumentsException("请输入-r后的值！");
                        RException.printStackTrace();//打印异常栈追踪信息
                        System.exit(1); // 退出程序
                    }
                    break;
                case "-r":
                    // 检测到 -r，获取后面的元素作为值
                    if (i + 1 < args.length) {
                        if (Util.judgeNotAllNumber(args[i + 1])) {
                            // 表面输入的值不为自然数
                            ArgumentsException NException = new ArgumentsException("请保证-r后的值为自然数！");
                            NException.printStackTrace();//打印异常栈追踪信息
                            System.exit(1); // 退出程序
                        }

                        // 此处如果收到了1.1，自动将其转型为1
                        range = Integer.parseInt(args[i + 1]);
                        if (Util.checkArgument(range)) {
                            // 输入的值不为自然数
                            ArgumentsException RException = new ArgumentsException("请保证-r后的值为自然数！");
                            RException.printStackTrace();//打印异常栈追踪信息
                            System.exit(1); // 退出程序
                        }
                    } else {
                        // 如果 -r 后没有元素，则创建异常对象
                        ArgumentsException RException = new ArgumentsException("请输入-r后的值！");
                        RException.printStackTrace();//打印异常栈追踪信息
                        System.exit(1); // 退出程序
                    }
                    break label;
                case "-e":
                    exerciseFilePath = args[i + 1];
                    break;
                case "-a":
                    answerFilePath = args[i + 1];
                    break;
            }
        }
        // 先判断是生成还是检测
        if (num != 0 && range != 0 && exerciseFilePath == null && answerFilePath == null){
            // 生成
            ArrayList<MathExpression> allExpression = generateAll(num, range);
            if(allExpression.size() == 0){
                System.out.println("生成表达式的过程中出错！");
                System.exit(1);
            }
            System.out.println("生成表达式成功！");
            long nowTimestamp = System.currentTimeMillis();
            System.out.println("用时：" + (nowTimestamp - rawTimestamp) / 1000 + "秒");
            // 将题目写入文件，注意空格
            // 提取出每行的答案，化简后放入
            ArrayList<String> exercises = new ArrayList<>();
            ArrayList<String> answers = new ArrayList<>();
            int count = 1;
            for (MathExpression me :
                    allExpression) {
                String formula = count + ". " + me.getFormOfFormula() + "=";
                // 遍历让每个符号两边都有空格
                String pattern = "([+\\-×÷=])";
                String result = formula.replaceAll(pattern, " $1 ");
                exercises.add(result);
                answers.add(Util.simplifyFraction(me.getValue().getForm()));
                count++;
            }
            // 存入文件
            if (!Util.saveToFile("Exercises.txt", exercises)){
                System.out.println("写入题目文件失败！");
                System.exit(1);
            }

            if (!Util.saveToFile("Answers.txt", answers)){
                System.out.println("写入答案文件失败！");
                System.exit(1);
            }
        } else if (num == 0 && range == 0 && exerciseFilePath != null && answerFilePath != null){
            // 检测
            if (!Util.readAndCalculate(exerciseFilePath, answerFilePath, "Grade.txt")){
                System.out.println("打分过程中出错！");
                System.exit(1);
            }
        } else {
            ArgumentsException RException = new ArgumentsException("输入有误！");
            RException.printStackTrace();//打印异常栈追踪信息
            System.exit(1); // 退出程序
        }
    }

    /**
     * 生成num个表达式
     * @param num 生成数量
     * @param range 每个值的大小范围，属于[0,range)
     * @return 返回每个表达式构成的数组
     */
    public static ArrayList<MathExpression> generateAll(int num, int range){
        ArrayList<MathExpression> allExpression = new ArrayList<>();
        ArrayList<ArrayList<MathExpression>> allExpressionStep = new ArrayList<>();
        while(num > 0){
            // 生成单个表达式
            MathExpression oneExpression;
            // 反复生成，直到不出现错误的表达式
            do{
                oneExpression = generateOne(range, allExpressionStep);
            }while (oneExpression == null);
            allExpression.add(oneExpression);
            num--;
        }
        return allExpression;
    }

    /**
     * 生成单个表达式
     * @param range 表达式值的上限
     * @param allExpressionStep 全部表达式
     * @return 生成的新表达式
     */
    public static MathExpression generateOne(int range, ArrayList<ArrayList<MathExpression>> allExpressionStep){
        // 随机产生运算符的数目，属于[1,NUMBER_OF_OPERATOR]
        Random random = new Random();
        int operatorNum = random.nextInt(NUMBER_OF_OPERATOR) + 1;
        ArrayList<Operator> operators = new ArrayList<>();
        // 产生的数的数量为运算符+1
        int numberNum = operatorNum + 1;
        ArrayList<MathExpression> figuresExpression = new ArrayList<>();

        for(int i = 1; i <= operatorNum; i++){
            Operator operator = new Operator(i, random.nextInt(4) + 1);
            operators.add(operator);
        }

        for(int i = 1; i <= numberNum; i++){
            // 创建一个随机数，封装到expression中去
            String valueStr = Util.generateFraction(range);
            // 计算生成数的值
            double value = Util.calculateFraction(valueStr);
            Figure figure = new Figure(value, valueStr, valueStr);
            figuresExpression.add(new MathExpression(figure, valueStr));
        }
        // 生成两个初始优先级数组，一个存放×÷，一个存放+-
        ArrayList<Operator> mulAndDevPriority = getPriority(operators, MULTIPLE, DEVICE); // ×÷
        ArrayList<Operator> pluAndMinPriority = getPriority(operators, PLUS, MINUS); // +-
        // 得到新的优先级
        ArrayList<Operator> newPriority = generatePriority(mulAndDevPriority, pluAndMinPriority);

        // 进行计算，考虑到负数和1/0的情况
        ArrayList<MathExpression> newExpressionStep = calculateResult(figuresExpression, mulAndDevPriority,
                pluAndMinPriority, newPriority, operators);
        if (newExpressionStep == null) return null;
        MathExpression result = newExpressionStep.get(newExpressionStep.size() - 1); // 得到最后一个元素

        // 判断是否重复，重复则重新生成
        if (judgeRepetitive(newExpressionStep, allExpressionStep)){
            return null;
        }

        allExpressionStep.add(newExpressionStep);
        // 最后一个元素即为最终整合结果
        return result;
    }

    /**
     * 得到运算符的初始优先级
     * @param operators 运算符数组
     * @param lowerBound 运算符下界
     * @param upperBound 运算符上界
     * @return 优先级数组，其中值越小代表优先级越高
     */
    public static ArrayList<Operator> getPriority(ArrayList<Operator> operators, int lowerBound,
                                                  int upperBound){
        // 规则很简单，针对一个类型的运算符，分别从左自右排优先级
        ArrayList<Operator> priority = new ArrayList<>();
        for (Operator operator : operators) {
            int value = operator.getValue();
            if (value >= lowerBound && value <= upperBound) {
                priority.add(operator); // 插入到新数组中
            }
        }
        return priority;
    }

    /**
     * 生成新的优先级
     * @param mulAndDevPriority 乘除数组
     * @param pluAndMinPriority 加减数组
     * @return 新优先级数组
     */
    public static ArrayList<Operator> generatePriority(ArrayList<Operator> mulAndDevPriority,
                                                       ArrayList<Operator> pluAndMinPriority){
        ArrayList<Operator> newPriority = new ArrayList<>();
        // 将两个数组的数据随机排列插入新数组中
        newPriority.addAll(mulAndDevPriority);
        newPriority.addAll(pluAndMinPriority);

        Collections.shuffle(newPriority); // 打乱优先级数组
        return newPriority;
    }

    /**
     * 计算最终的结果
     * @param figuresExpression 每个封装为MathExpression的数值
     * @param mulAndDevPriority 乘除数组
     * @param pluAndMinPriority 加减数组
     * @param newPriority 新的优先级数组
     * @param referencePosition 用以参考数字位置的数组
     * @return 每一步计算的表达式，包含表达式的值和字符串形式，按照先后顺序组成了数组
     */
    public static ArrayList<MathExpression> calculateResult(ArrayList<MathExpression> figuresExpression,
                                                            ArrayList<Operator> mulAndDevPriority,
                                                            ArrayList<Operator> pluAndMinPriority,
                                                            ArrayList<Operator> newPriority,
                                                            ArrayList<Operator> referencePosition){
        // 创建新数组，复制figuresExpression
        ArrayList<MathExpression> step = new ArrayList<>();
        for (Operator op : newPriority) {
            // 按照优先级数组，依次计算
            // 判断是否需要给运算符添加括号
            if (isNeedToAddBracket(op, mulAndDevPriority, pluAndMinPriority, referencePosition)) {
                op.setNeedBracket(true);
            }
            // 在对应的运算符数组中删除本次运算的运算符
            int opType = op.getOpType();
            if (opType == PlusAndMinTypeMark) {
                deleteOperator(op, pluAndMinPriority);
            } else if (opType == MulAndDevTypeMark) {
                deleteOperator(op, mulAndDevPriority);
            }

            // 计算两个封装后的Figure
            // 删除运算符并返回其下标
            int indexOfFormerElement = referencePosition.indexOf(op);
            int indexOfLatterFigure = indexOfFormerElement + 1;
            // 计算结果
            MathExpression afterIntegration = calculateFigure(figuresExpression.get(indexOfFormerElement),
                    figuresExpression.get(indexOfLatterFigure), op);
            if (afterIntegration == null) return null;
            // 删除后者
            if (figuresExpression.size() != 1) {
                figuresExpression.remove(indexOfLatterFigure);
            }
            referencePosition.remove(indexOfFormerElement); // 删除参考数组中的运算符

            // 将前者进行复制，放入列表中
            MathExpression newME = new MathExpression(afterIntegration.getValue(), afterIntegration.getFormOfFormula());
            newME.setNeedSimplify(false);// 标记不需要化简了
            // 记录步骤
            figuresExpression.set(indexOfFormerElement, newME);
            step.add(afterIntegration);
        }
        return step;
    }

    /**
     * 计算单个运算符表达式
     * @param formerFigure 左值
     * @param latterFigure 右值
     * @param operator 运算符
     * @return 得到的结果
     */
    public static MathExpression calculateFigure(MathExpression formerFigure, MathExpression latterFigure,
                                                 Operator operator){
        // 计算将得到一个新的表达式
        Figure former = formerFigure.getValue();
        Figure latter = latterFigure.getValue();
        // 判断是否需要化简
        boolean formerSimplify = formerFigure.isNeedSimplify();
        boolean latterSimplify = latterFigure.isNeedSimplify();
        // 计算值
        Figure newFigure = Util.calculateFractionExpression(former, latter, operator);
        if (newFigure == null) return null;
        formerFigure.setValue(newFigure);
        // 将表达式合并
        // 检测是否要加括号
        boolean isNeedBracket = operator.isNeedBracket();
        String formerForm = formerFigure.getFormOfFormula();
        String latterForm = latterFigure.getFormOfFormula();
        // 化简分数，此处是为了显示表达式的时候呈现化简形式
        if (formerSimplify){
            formerForm = Util.simplifyFraction(formerForm);
        }
        if (latterSimplify){
            latterForm = Util.simplifyFraction(latterForm);
        }

        String op = "WRONG";
        switch (operator.getValue()){
            case 1:
                op = "+";
                break;
            case 2:
                op = "-";
                break;
            case 3:
                op = "×";
                break;
            case 4:
                op = "÷";
                break;
        }
        if (isNeedBracket){
            formerFigure.setFormOfFormula("(" + formerForm + op + latterForm + ")");
        } else {
            formerFigure.setFormOfFormula(formerForm + op + latterForm);
        }
        return formerFigure;
    }

    /**
     * 检测是否存在重复
     * @param newExpressionStep 生成新的表达式的步骤
     * @param allExpressionStep 以往生成的全部表达式的步骤
     * @return true表明重复，false表明不重复
     */
    public static boolean judgeRepetitive(ArrayList<MathExpression> newExpressionStep,
                                          ArrayList<ArrayList<MathExpression>> allExpressionStep){
        // 将newStep的步骤按照字符串数组读出
        ArrayList<String> newStep = new ArrayList<>();
        for (MathExpression me :
                newExpressionStep) {
            newStep.add(me.getValue().getRecordStep());
        }
        for (ArrayList<MathExpression> me :
                allExpressionStep) {
            ArrayList<String> passedStep = new ArrayList<>();
            for (MathExpression m :
                    me) {
                passedStep.add(m.getValue().getRecordStep());
            }
            // 将两字符串元素进行比较，若完全一样(不包含顺序)，则表明重复
            if (Util.judgeRepeatElement(newStep, passedStep)){
                System.out.println("出现重复");
                // 每个记录步骤的最后一步都是完整表达式
                System.out.println("新生成的表达式为：" + newExpressionStep.
                        get(newExpressionStep.size() - 1).getFormOfFormula());
                System.out.println("与之重复的表达式为：" + me.get(me.size() - 1).getFormOfFormula() + '\n');
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否需要添加括号
     * @param op 运算符
     * @param mulAndDevPriority 乘除数组
     * @param pluAndMinPriority 加减数组
     * @return true表明需要添加括号，false表明不需要添加括号
     */
    public static boolean isNeedToAddBracket(Operator op, ArrayList<Operator> mulAndDevPriority,
                                             ArrayList<Operator> pluAndMinPriority,
                                             ArrayList<Operator> referencePosition){
        // 判断是否需要括号
        boolean needFlag = false;
        int opType = op.getOpType();
        // 如果是加减运算符则需要判断乘除数组的情况
        if (opType == PlusAndMinTypeMark){
            if (mulAndDevPriority.size() != 0){
                if (pluAndMinPriority.size() > 1 && op.getId() == pluAndMinPriority.get(0).getId()){
                    // 当此操作符为加减数组的首位时，则判断与第二位的是否挨着
                    long id = pluAndMinPriority.get(1).getId();
                    // 找到第一位运算符在referencePosition中的下标
                    int i = referencePosition.indexOf(pluAndMinPriority.get(0));
                    // 往后找最近的一个运算符，要考虑不要越过下界
                    int j = i + 1;
                    if (j < referencePosition.size()){
                        if (referencePosition.get(j).getId() != id){
                            needFlag = true;
                        }
                    }
                } else {
                    needFlag = true;
                }
            } else {
                // 设置>1是防止只剩一个运算符时也给两个操作数加括号，因为只剩一个运算符的话，优先级数组首位必然是它自己
                if (pluAndMinPriority.size() > 1 && op.getId() != pluAndMinPriority.get(0).getId()){
                    needFlag = true;
                }
            }
        } else if (opType == MulAndDevTypeMark) {
            // 设置>1是防止只剩一个运算符时也给两个操作数加括号，因为只剩一个运算符的话，优先级数组首位必然是它自己
            if (mulAndDevPriority.size() > 1 && op.getId() != mulAndDevPriority.get(0).getId()){
                needFlag = true;
            }
        }
        return needFlag;
    }

    /**
     * 删除运算符
     * @param op 运算符
     * @param operators 运算符数组
     */
    public static void deleteOperator(Operator op, ArrayList<Operator> operators){
        long opId = op.getId();
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).getId() == opId){
                operators.remove(i);
                break;
            }
        }
    }
}

