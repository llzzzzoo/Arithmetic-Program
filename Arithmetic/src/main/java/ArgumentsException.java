public class ArgumentsException extends Exception{
    /**
     * 打印参数输入的异常
     * @param message 输入参数错误的信息
     */
    public ArgumentsException(String message) {
        super(message);
    }
}