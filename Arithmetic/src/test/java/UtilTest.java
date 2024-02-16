import org.junit.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void checkArgument() {
        assertFalse(Util.checkArgument(1));
    }

    @Test
    public void generateFraction() {
        assertNotNull(Util.generateFraction(10));
    }

    @Test
    public void calculateFraction() {
        long result1 = (long) Util.calculateFraction("3/2");
        long expected1 = (long) 1.5;
        assertEquals(expected1, result1);

        long result2 = (long) Util.calculateFraction("3/0");
        assertEquals(-1, result2);
    }

    @Test
    public void calculateFractionExpression() {
        Figure f1 = new Figure(1, "1/1", "1/1");
        Figure f2 = new Figure(2, "2/1", "2/1");
        Operator op = new Operator(1,1);
        Figure result = Util.calculateFractionExpression(f1, f2, op);
        assertNotNull(result);
        assertEquals("3/1", result.getForm());
    }

    @Test
    public void simplifyFraction() {
        String s1 = "9/6";
        String s2 = "2/2";
        String s3 = "0/2";
        String s4 = "2/1";
        String s5 = "3/2";

        assertEquals("1'1/2", Util.simplifyFraction(s1));
        assertEquals("1", Util.simplifyFraction(s2));
        assertEquals("0", Util.simplifyFraction(s3));
        assertEquals("2", Util.simplifyFraction(s4));
        assertEquals("1'1/2", Util.simplifyFraction(s5));
    }

    @Test
    public void gcd() {
        assertEquals(3, Util.gcd(9, 6));
    }

    @Test
    public void fractionToMixedNumber() {
        String s1 = "9/6";
        String s2 = "2/2";
        String s3 = "0/2";

        assertEquals("1'3/6", Util.fractionToMixedNumber(s1));
        assertEquals("1", Util.fractionToMixedNumber(s2));
        assertEquals("0", Util.fractionToMixedNumber(s3));
    }

    @Test
    public void convertExpression() {
        assertEquals("9/6+1", Util.convertExpression("1'3/6+1"));
    }

    @Test
    public void convertFraction() {
        assertEquals("9/6", Util.convertFraction("1'3/6"));
    }

    @Test
    public void saveToFile() {
        ArrayList<String> list = new ArrayList<>();
        list.add("test");
        assertTrue(Util.saveToFile("test.txt", list));
    }

    @Test
    public void readAndCalculate() {
        assertTrue(Util.readAndCalculate("Exercises.txt", "Answers.txt",
                "Grade.txt"));
    }

    @Test
    public void calculate() {
        long result1 = (long) Util.calculate("(1+(4/3-1))×3÷4");
        assertEquals(1, result1);

        // 创建一个DecimalFormat对象，指定保留四位小数
        DecimalFormat df = new DecimalFormat("#.####");
        // 使用format方法进行四舍五入
        String formattedNumber = df.format(Util.calculate("(1+4/3-1)×3÷4"));
        assertEquals("1", formattedNumber);
    }

    @Test
    public void isOperator() {
        assertTrue(Util.isOperator('+'));
        assertTrue(Util.isOperator('-'));
        assertTrue(Util.isOperator('×'));
        assertTrue(Util.isOperator('÷'));
        assertFalse(Util.isOperator('a'));
    }

    @Test
    public void hasPrecedence() {
        assertTrue(Util.hasPrecedence('+', '-'));
        assertTrue(Util.hasPrecedence('+', '×'));
        assertTrue(Util.hasPrecedence('+', '÷'));
        assertTrue(Util.hasPrecedence('-', '×'));
        assertTrue(Util.hasPrecedence('-', '÷'));
        assertTrue(Util.hasPrecedence('×', '÷'));
        assertTrue(Util.hasPrecedence('+', '+'));
        assertTrue(Util.hasPrecedence('-', '-'));
        assertTrue(Util.hasPrecedence('×', '×'));
        assertTrue(Util.hasPrecedence('÷', '÷'));
        assertFalse(Util.hasPrecedence('×', '+'));
        assertFalse(Util.hasPrecedence('÷', '-'));
        assertFalse(Util.hasPrecedence('+', '('));
        assertFalse(Util.hasPrecedence('+', ')'));
    }

    @Test
    public void applyOperator() {
        assertEquals(2, Util.applyOperator('+', 1, 1), 0.0001);
        assertEquals(0, Util.applyOperator('-', 1, 1), 0.0001);
        assertEquals(1, Util.applyOperator('×', 1, 1), 0.0001);
        assertEquals(1, Util.applyOperator('÷', 1, 1), 0.0001);
    }

        @Test
    public void judgeNotAllNumber() {
        assertTrue(Util.judgeNotAllNumber("1+1"));
        assertFalse(Util.judgeNotAllNumber("1"));
    }

    @Test
    public void readAnswerFile() {
        ArrayList<Double> list = Util.readAnswerFile("Answers.txt");
        assertNotNull(list);
    }

    @Test
    public void judgeCorrectAndWrong() {
        ArrayList<Double> list1 = new ArrayList<>();
        list1.add(1.0);
        list1.add(2.0);
        list1.add(3.0);
        ArrayList<Double> list2 = new ArrayList<>();
        list2.add(1.0);
        list2.add(2.0);
        list2.add(3.0);
        ArrayList<Double> list3 = new ArrayList<>();
        list3.add(1.0);
        list3.add(2.0);
        list3.add(5.0);
        ArrayList<Double> list4 = new ArrayList<>();
        list4.add(1.0);
        list4.add(2.0);
        list4.add(4.0);
        ArrayList<Integer> correct1 = new ArrayList<>();
        ArrayList<Integer> wrong1 = new ArrayList<>();
        ArrayList<Integer> correct2 = new ArrayList<>();
        ArrayList<Integer> wrong2 = new ArrayList<>();

        Util.judgeCorrectAndWrong(list1, list2, correct1, wrong1);
        Util.judgeCorrectAndWrong(list3, list4, correct2, wrong2);

        assertEquals(3, correct1.size());
        assertEquals(0, wrong1.size());
        assertEquals(2, correct2.size());
        assertEquals(1, wrong2.size());
    }

    @Test
    public void generateGradeReport() {
        ArrayList<Integer> correct = new ArrayList<>();
        ArrayList<Integer> wrong = new ArrayList<>();
        ArrayList<String> content = new ArrayList<>();

        correct.add(1);
        correct.add(2);
        wrong.add(3);

        Util.generateGradeReport(correct, wrong, content);

        assertEquals("Correct: 2 (1, 2)", content.get(0));
        assertEquals("Wrong: 1 (3)", content.get(1));
    }

    @Test
    public void judgeRepeatElement() {
        // 测试1+2+3和3+(2+1)重复
        ArrayList<String> newStep1 = new ArrayList<>();
        ArrayList<String> passedStep1 = new ArrayList<>();

        newStep1.add("2/1+1/1");
        newStep1.add("3/1+3/1");
        passedStep1.add("1/1+2/1");
        passedStep1.add("3/1+3/1");

        assertTrue(Util.judgeRepeatElement(newStep1, passedStep1));

        // 测试1+2+3和2+1不重复
        ArrayList<String> newStep2 = new ArrayList<>();
        ArrayList<String> passedStep2 = new ArrayList<>();

        newStep2.add("2/1+1/1");
        passedStep2.add("1/1+2/1");
        passedStep2.add("3/1+3/1");

        assertFalse(Util.judgeRepeatElement(newStep2, passedStep2));
    }

    @Test
    public void areExpressionsEquivalent() {
        assertTrue(Util.areExpressionsEquivalent("1+2", "2+1"));
        assertTrue(Util.areExpressionsEquivalent("1/3+2/1", "2/1+1/3"));
        assertTrue(Util.areExpressionsEquivalent("1×2", "2×1"));
        assertTrue(Util.areExpressionsEquivalent("1×20/13", "20/13×1"));
        assertFalse(Util.areExpressionsEquivalent("2-1", "1-2"));
        assertFalse(Util.areExpressionsEquivalent("1÷2", "2÷1"));
        assertFalse(Util.areExpressionsEquivalent("1/20-2", "2-1/20"));
        assertFalse(Util.areExpressionsEquivalent("12/5÷2", "2÷12/5"));
    }
}