package com.lotus.lotusapp.utils;

/**
 * 校验密码工具类
 */
public class PasswordRuleUtil {

    /**
     * 校验密码
     *
     * @param rule
     * @param password
     * @return
     */
    public static boolean checkPassword(String rule, String password) {
        try {
            // 密码规则转换int数组
            int[] arr = new int[rule.length()];
            for (int i = 0; i < rule.length(); i++) {
                arr[i] = Integer.parseInt(rule.substring(i, i + 1));
            }
            // 根据奇偶数获取三次校验规则
            int one = (arr[0] & 1) == 1 ? -arr[1] : arr[1];
            int two = (arr[2] & 1) == 1 ? -arr[3] : arr[3];
            int three = (arr[4] & 1) == 1 ? -arr[5] : arr[5];
            password = password.substring(password.indexOf("00"));
            // 校验第一次规则
            arr = new int[password.length()];
            for (int i = 0; i < password.length(); i++) {
                arr[i] = Integer.parseInt(password.substring(i, i + 1));
            }
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != 0) {
                    if (checkRule(arr, one, i)) return false;
                    password = password.substring(i + 2);
                    break;
                }
            }
            // 校验第二次规则
            arr = new int[password.length()];
            for (int i = 0; i < password.length(); i++) {
                arr[i] = Integer.parseInt(password.substring(i, i + 1));
            }
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != 0) {
                    if (checkRule(arr, two, i)) return false;
                    password = password.substring(i + 2);
                    break;
                }
            }
            // 校验第三次规则
            arr = new int[password.length()];
            for (int i = 0; i < password.length(); i++) {
                arr[i] = Integer.parseInt(password.substring(i, i + 1));
            }
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != 0) {
                    if (checkRule(arr, three, i)) return false;
                    break;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    /**
     * 校验规则
     *
     * @param arr
     * @param rule
     * @param i
     * @return
     */
    private static boolean checkRule(int[] arr, int rule, int i) {
        if (rule > 0) {
            if (arr[i] + rule >= 10) {
                if (arr[i] + rule - 10 != arr[i + 1]) {
                    return true;
                }
            } else {
                if (arr[i] + rule != arr[i + 1]) {
                    return true;
                }
            }
        }
        if (rule < 0) {
            if (Math.abs(arr[i] + rule) != arr[i + 1]) {
                return true;
            }
        }
        return false;
    }
}
