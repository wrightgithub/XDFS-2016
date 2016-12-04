package com.xdfs.common.util;

/**
 * Created by xyy on 16-12-5.
 */

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericsUtils {

    /**
     * 7 * ͨ������,��ö���Classʱ�����ĸ���ķ��Ͳ���������. 8 * ��public BookManager extends GenricManager<Book> 9 * 10 * @param clazz The
     * class to introspect 11 * @return the first generic declaration, or <code>Object.class</code> if cannot be
     * determined 12
     */
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 18 * ͨ������,��ö���Classʱ�����ĸ���ķ��Ͳ���������.
     * 19 * ��public BookManager extends GenricManager<Book>
     *  @param clazz clazz The class to introspect 2
     *  @param index the Index of the generic ddeclaration,start from 0. 23
     */
    public static Class getSuperClassGenricType(Class clazz, int index) throws IndexOutOfBoundsException {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }
}
