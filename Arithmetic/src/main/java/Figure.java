public class Figure {
    private final double value; // 存放计算得到的值
    private final String form; // 存放分数的表达形式
    private final String recordStep; // 记录得到这个数字所经历的上一个计算步骤，格式：操作数 (运算符 操作数)

    public Figure(double value, String form, String recordStep) {
        this.value = value;
        this.form = form;
        this.recordStep = recordStep;
    }

    public double getValue() {
        return value;
    }

    public String getForm() {
        return form;
    }

    public String getRecordStep() {
        return recordStep;
    }
}
