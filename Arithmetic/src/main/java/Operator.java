public class Operator {
    private final long id; // 运算符号的id
    private final int value; // 表面运算符号的类型。1:2:3:4 = +:-:×:÷
    private boolean isNeedBracket = false; // 标记该运算符两个操作数两侧是否需要括号，true表明需要，false表明不需要
    private int opType = 0; // 为1表示为加减运算符，为2表示为乘除运算符

    public Operator(long id, int value) {
        this.id = id;
        this.value = value;
        // 在此处判断
        if (value <= 2){
            opType = 1;
        } else if (value <= 4) {
            opType = 2;
        }
    }

    public long getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public boolean isNeedBracket() {
        return isNeedBracket;
    }

    public void setNeedBracket(boolean needBracket) {
        isNeedBracket = needBracket;
    }

    public int getOpType() {
        return opType;
    }
}
