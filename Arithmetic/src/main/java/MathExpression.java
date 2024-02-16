public class MathExpression {
    private Figure value; // 对应表达式计算得到的值
    private String formOfFormula; // 表达式的形式
    private boolean isNeedSimplify; // 标记该表达式是否需要化简

    public MathExpression(Figure value, String formOfFormula) {
        this.value = value;
        this.formOfFormula = formOfFormula;
        this.isNeedSimplify = true;
    }

    public Figure getValue() {
        return value;
    }

    public void setValue(Figure value) {
        this.value = value;
    }

    public String getFormOfFormula() {
        return formOfFormula;
    }

    public void setFormOfFormula(String formOfFormula) {
        this.formOfFormula = formOfFormula;
    }

    public boolean isNeedSimplify() {
        return isNeedSimplify;
    }

    public void setNeedSimplify(boolean needSimplify) {
        isNeedSimplify = needSimplify;
    }
}
