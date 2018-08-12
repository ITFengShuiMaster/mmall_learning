package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Luyue
 * @date 2018/7/31 10:50
 **/
@Service("iCategoryService")
@Slf4j
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        if (categoryMapper.insert(category) > 0) {
            return ServerResponse.createBySuccessMessage("添加商品分类成功");
        }
        return ServerResponse.createByErrorMessage("添加商品分类失败");
    }

    @Override
    public ServerResponse updateCategoryName(String categoryName, Integer categoryId) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        if (categoryMapper.updateByPrimaryKeySelective(category) > 0) {
            return ServerResponse.createBySuccess();
        }

        return ServerResponse.createByErrorMessage("更新商品分类失败");
    }

    @Override
    public ServerResponse<List<Category>> getParallelCategory(Integer categoryId) {
        if (categoryId == null) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        List<Category> categories = categoryMapper.selectCategoryByParentId(categoryId);
        if (CollectionUtils.isEmpty(categories)) {
            log.warn(categoryId + "无子分类");
        }
        return ServerResponse.createBySuccess(categories);
    }

    @Override
    public ServerResponse<List<Integer>> getDeepCategory(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        deepSelectCategory(categoryId, categorySet);

        List<Integer> categoryIdList = Lists.newArrayList();
        for (Category ca : categorySet) {
            categoryIdList.add(ca.getId());
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    private Set<Category> deepSelectCategory(Integer categoryId, Set<Category> categorySet) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }

        List<Category> categoryList = categoryMapper.selectCategoryByParentId(categoryId);
        for (Category ca : categoryList) {
            deepSelectCategory(ca.getId(), categorySet);
        }

        return categorySet;
    }
}
