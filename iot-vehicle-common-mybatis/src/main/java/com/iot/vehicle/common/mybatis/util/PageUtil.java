package com.iot.vehicle.common.mybatis.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页工具类
 *
 * @author dongxiang.wu
 */
public class PageUtil {

    /**
     * 默认页码
     */
    private static final long DEFAULT_CURRENT = 1L;

    /**
     * 默认每页大小
     */
    private static final long DEFAULT_SIZE = 10L;

    /**
     * 最大每页大小
     */
    private static final long MAX_SIZE = 100L;

    /**
     * 创建分页对象
     *
     * @param current 当前页码
     * @param size    每页大小
     * @param <T>     数据类型
     * @return 分页对象
     */
    public static <T> Page<T> getPage(Long current, Long size) {
        // 处理null值
        if (current == null || current < 1) {
            current = DEFAULT_CURRENT;
        }
        if (size == null || size < 1) {
            size = DEFAULT_SIZE;
        }
        
        // 限制最大每页大小，防止查询过多数据
        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }
        
        return new Page<>(current, size);
    }

    /**
     * 创建分页对象（使用默认值）
     *
     * @param <T> 数据类型
     * @return 分页对象
     */
    public static <T> Page<T> getPage() {
        return new Page<>(DEFAULT_CURRENT, DEFAULT_SIZE);
    }

    /**
     * 创建分页对象（指定页码，使用默认每页大小）
     *
     * @param current 当前页码
     * @param <T>     数据类型
     * @return 分页对象
     */
    public static <T> Page<T> getPage(Long current) {
        return getPage(current, DEFAULT_SIZE);
    }

    /**
     * 计算总页数
     *
     * @param total 总记录数
     * @param size  每页大小
     * @return 总页数
     */
    public static long getPages(long total, long size) {
        if (size == 0) {
            return 0;
        }
        return (total + size - 1) / size;
    }

    /**
     * 计算偏移量（用于LIMIT查询）
     *
     * @param current 当前页码
     * @param size    每页大小
     * @return 偏移量
     */
    public static long getOffset(long current, long size) {
        return (current - 1) * size;
    }
}

