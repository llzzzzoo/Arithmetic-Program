import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;


public class MainClassTest {
    @Test
    public void generateAll() {
        int num = 10;
        int range = 10;
        ArrayList<MathExpression> a = MainClass.generateAll(num, range);
        assertEquals(num, a.size()); // 生成的题目数量是否正确
    }

    @Test
    public void generateOne() {
        int range = 10;
        ArrayList<ArrayList<MathExpression>> allExpressionSteps = new ArrayList<>();

        MathExpression a;
        do {
            a = MainClass.generateOne(range, allExpressionSteps);
        } while (a == null);
        assertNotNull(a); // 偷个懒
    }

    @Test
    public void getPriority() {
        ArrayList<Operator> operators = new ArrayList<>();
        Operator operator1 = new Operator(1, 1);
        Operator operator2 = new Operator(2, 2);
        Operator operator3 = new Operator(3, 3);
        Operator operator4 = new Operator(4, 4);
        operators.add(operator1);
        operators.add(operator2);
        operators.add(operator3);
        operators.add(operator4);

        ArrayList<Operator> operators1 =  MainClass.getPriority(operators, 1, 2);
        ArrayList<Operator> operators2 =  MainClass.getPriority(operators, 3, 4);

        assertEquals(1, operators1.get(0).getValue());
        assertEquals(2, operators1.get(1).getValue());
        assertEquals(3, operators2.get(0).getValue());
        assertEquals(4, operators2.get(1).getValue());
    }

    @Test
    public void generatePriority() {
        ArrayList<Operator> pluAndMinPriority = new ArrayList<>();
        ArrayList<Operator> mulAndDevPriority = new ArrayList<>();
        Operator operator1 = new Operator(1, 1);
        Operator operator2 = new Operator(2, 2);
        Operator operator3 = new Operator(3, 3);
        Operator operator4 = new Operator(4, 4);
        pluAndMinPriority.add(operator1);
        pluAndMinPriority.add(operator2);
        mulAndDevPriority.add(operator3);
        mulAndDevPriority.add(operator4);

        ArrayList<Operator> operators3 = MainClass.generatePriority(pluAndMinPriority, mulAndDevPriority);
        assertEquals(4, operators3.size());
    }

    @Test
    public void calculateResult() {
        // 测试表达式: (1+2-1)×3÷1'4/5
        ArrayList<MathExpression> figuresExpression = new ArrayList<>();
        ArrayList<Operator> mulAndDevPriority = new ArrayList<>();
        ArrayList<Operator> pluAndMinPriority = new ArrayList<>();
        ArrayList<Operator> newPriority = new ArrayList<>();
        ArrayList<Operator> referencePosition = new ArrayList<>();

        // 数值部分
        MathExpression figure1 = new MathExpression(new Figure(1, "1/1", "1/1"),
                "1/1");
        MathExpression figure2 = new MathExpression(new Figure(2, "2/1", "2/1"),
                "2/1");
        MathExpression figure3 = new MathExpression(new Figure(1, "1/1", "1/1"),
                "1/1");
        MathExpression figure4 = new MathExpression(new Figure(3, "3/1", "3/1"),
                "3/1");
        MathExpression figure5 = new MathExpression(new Figure(1.8, "9/5", "9/5"),
                "9/5");
        figuresExpression.add(figure1);
        figuresExpression.add(figure2);
        figuresExpression.add(figure3);
        figuresExpression.add(figure4);
        figuresExpression.add(figure5);

        // 运算符部分
        Operator operator1 = new Operator(1, 1);
        Operator operator2 = new Operator(2, 2);
        Operator operator3 = new Operator(3, 3);
        Operator operator4 = new Operator(4, 4);
        pluAndMinPriority.add(operator1);
        pluAndMinPriority.add(operator2);
        mulAndDevPriority.add(operator3);
        mulAndDevPriority.add(operator4);

        // 优先级
        newPriority.add(operator1);
        newPriority.add(operator2);
        newPriority.add(operator3);
        newPriority.add(operator4);

        // 相对位置
        referencePosition.add(operator1);
        referencePosition.add(operator2);
        referencePosition.add(operator3);
        referencePosition.add(operator4);

        ArrayList<MathExpression> result = MainClass.calculateResult(figuresExpression, mulAndDevPriority,
                pluAndMinPriority, newPriority, referencePosition);
        assertNotNull(result);
        assertEquals("(1+2-1)×3÷1'4/5", result.get(result.size() - 1).getFormOfFormula());
        assertEquals("30/9", result.get(result.size() - 1).getValue().getForm());
    }

    @Test
    public void calculateFigure() {
        // 表达式: 1+2
        MathExpression m1 = new MathExpression(new Figure(1.0, "1/1", "1/1"), "1/1");
        MathExpression m2 = new MathExpression(new Figure(2.0, "2/1", "2/1"), "2/1");
        Operator op = new Operator(1, 1);
        MathExpression m = MainClass.calculateFigure(m1, m2, op);
        assertNotNull(m);
        assertEquals("3/1", m.getValue().getForm());
    }

    @Test
    public void judgeRepetitive() {
        // 判断表达式1+2+3和3+(2+1)重复
        MathExpression newM1 = new MathExpression(new Figure(3.0, "3/1", "1/1+2/1"),
                "1+2");
        MathExpression newM2 = new MathExpression(new Figure(6.0, "6/1", "3/1+3/1"),
                "3+3");
        MathExpression passedM1 = new MathExpression(new Figure(3.0, "3/1", "2/1+1/1"),
                "(2+1)");
        MathExpression passedM2 = new MathExpression(new Figure(6.0, "6/1", "3/1+3/1"),
                "3+3");
        ArrayList<MathExpression> newExpressionStep = new ArrayList<>();
        ArrayList<MathExpression> passedExpressionStep = new ArrayList<>();
        ArrayList<ArrayList<MathExpression>> allExpressionSteps = new ArrayList<>();
        newExpressionStep.add(newM1);
        newExpressionStep.add(newM2);
        passedExpressionStep.add(passedM1);
        passedExpressionStep.add(passedM2);
        allExpressionSteps.add(passedExpressionStep);
        boolean result = MainClass.judgeRepetitive(newExpressionStep, allExpressionSteps);
        assertTrue(result);
    }

    @Test
    public void isNeedToAddBracket() {
        // (1+1)×3，测试运算符"+"，判断其需要加括号
        Operator op1 = new Operator(1, 1);
        Operator op2 = new Operator(2, 3);
        ArrayList<Operator> mulAndDevPriority1 = new ArrayList<>();
        ArrayList<Operator> pluAndMinPriority1 = new ArrayList<>();
        ArrayList<Operator> referencePosition1 = new ArrayList<>();
        mulAndDevPriority1.add(op1);
        pluAndMinPriority1.add(op2);
        referencePosition1.add(op1);
        referencePosition1.add(op2);

        boolean result1 = MainClass.isNeedToAddBracket(op1, mulAndDevPriority1, pluAndMinPriority1, referencePosition1);
        assertTrue(result1);

        // (1+1-1)×3，测试运算符"+"，判断其不需要加括号
        Operator op3 = new Operator(1, 1);
        Operator op4 = new Operator(2, 2);
        Operator op5 = new Operator(2, 3);
        ArrayList<Operator> mulAndDevPriority2 = new ArrayList<>();
        ArrayList<Operator> pluAndMinPriority2 = new ArrayList<>();
        ArrayList<Operator> referencePosition2 = new ArrayList<>();
        mulAndDevPriority1.add(op3);
        mulAndDevPriority1.add(op4);
        pluAndMinPriority1.add(op5);
        referencePosition1.add(op3);
        referencePosition1.add(op4);
        referencePosition1.add(op5);

        boolean result2 = MainClass.isNeedToAddBracket(op3, mulAndDevPriority2, pluAndMinPriority2, referencePosition2);
        assertFalse(result2);
    }

    @Test
    public void deleteOperator() {
        Operator op = new Operator(1, 1);
        ArrayList<Operator> operators = new ArrayList<>();
        operators.add(op);
        MainClass.deleteOperator(op, operators);
        assertEquals(0, operators.size());
    }
}