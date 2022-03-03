package com.exp.toolkit.basic;

import java.util.Arrays;

public final class ArrayUtils {
    
    /**
     * 判断指定元素与数组中哪一个下标的元素{@code equals}相等
     *
     * @param ar 数组
     * @param t   元素
     * @return  返回匹配元素在数组中的下标，或者数组中不包含匹配元素时返回-1
     */
    public static int indexOf(Object[] ar, Object t) {
        int len = ar.length;
        for (int i = 0; i < len; i++) {
            if (ar[i].equals(t)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * 获取元素在数组中的下标
     *
     * @param ar 数组
     * @param t   元素
     * @return 返回元素在数组中的下标，或者数组中包含此元素时返回-1
     */
    public static int exactIndexOf(Object[] ar, Object t) {
        int len = ar.length;
        for (int i = 0; i < len; i++) {
            if (ar[i] == t) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * 获取元素在数组中的下标
     *
     * @param ia 数组
     * @param i  元素
     * @return  返回元素在数组中的下标，或者数组中包含此元素时返回-1
     */
    public static int indexOf(int[] ia, int i) {
        int len = ia.length;
        for (int j = 0; j < len; j++) {
            if (ia[j] == i) {
                return j;
            }
        }
        return -1;
    }
    
    /**
     * @see #indexOf(int[], int)
     */
    public static int indexOf(long[] la, long l) {
        int len = la.length;
        for (int i = 0; i < len; i++) {
            if (la[i] == l) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * @see #indexOf(int[], int)
     */
    public static int indexOf(float[] fa, float f) {
        int len = fa.length;
        for (int i = 0; i < len; i++) {
            if (fa[i] == f) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * @see #indexOf(int[], int)
     */
    public static int indexOf(double[] da, double d) {
        int len = da.length;
        for (int i = 0; i < len; i++) {
            if (da[i] == d) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * 将原数组倒序
     *
     * @param arr 原数组
     */
    public static void reverse(Object[] arr) {
        int l = arr.length;
        for (int i = 0, half = arr.length / 2; i < half; i ++) {
            Object tmp = arr[i];
            int r = l - 1 - i;
            arr[i] = arr[r];
            arr[r] = tmp;
        }
    }
    
    /**
     * @see #reverse(Object[])
     */
    public static void reverse(int[] arr) {
        int l = arr.length;
        for (int i = 0, half = arr.length / 2; i < half; i ++) {
            int tmp = arr[i];
            int r = l - 1 - i;
            arr[i] = arr[r];
            arr[r] = tmp;
        }
    }
    
    /**
     * @see #reverse(Object[])
     */
    public static void reverse(long[] arr) {
        int l = arr.length;
        for (int i = 0, half = arr.length / 2; i < half; i ++) {
            long tmp = arr[i];
            int r = l - 1 - i;
            arr[i] = arr[r];
            arr[r] = tmp;
        }
    }
    
    /**
     * @see #reverse(Object[])
     */
    public static void reverse(float[] arr) {
        int l = arr.length;
        for (int i = 0, half = arr.length / 2; i < half; i ++) {
            float tmp = arr[i];
            int r = l - 1 - i;
            arr[i] = arr[r];
            arr[r] = tmp;
        }
    }
    
    /**
     * @see #reverse(Object[])
     */
    public static void reverse(double[] arr) {
        int l = arr.length;
        for (int i = 0, half = arr.length / 2; i < half; i ++) {
            double tmp = arr[i];
            int r = l - 1 - i;
            arr[i] = arr[r];
            arr[r] = tmp;
        }
    }
    
}
