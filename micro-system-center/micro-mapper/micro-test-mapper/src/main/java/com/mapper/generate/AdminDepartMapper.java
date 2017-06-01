package com.mapper.generate;

import com.entity.AdminDepart;
import com.entity.AdminDepartExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminDepartMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_depart
     *
     * @mbggenerated Thu Jun 01 15:52:36 CST 2017
     */
    int countByExample(AdminDepartExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_depart
     *
     * @mbggenerated Thu Jun 01 15:52:36 CST 2017
     */
    int deleteByExample(AdminDepartExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_depart
     *
     * @mbggenerated Thu Jun 01 15:52:36 CST 2017
     */
    int deleteByPrimaryKey(Integer departId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_depart
     *
     * @mbggenerated Thu Jun 01 15:52:36 CST 2017
     */
    int insert(AdminDepart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_depart
     *
     * @mbggenerated Thu Jun 01 15:52:36 CST 2017
     */
    int insertSelective(AdminDepart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_depart
     *
     * @mbggenerated Thu Jun 01 15:52:36 CST 2017
     */
    List<AdminDepart> selectByExample(AdminDepartExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_depart
     *
     * @mbggenerated Thu Jun 01 15:52:36 CST 2017
     */
    AdminDepart selectByPrimaryKey(Integer departId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_depart
     *
     * @mbggenerated Thu Jun 01 15:52:37 CST 2017
     */
    int updateByExampleSelective(@Param("record") AdminDepart record, @Param("example") AdminDepartExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_depart
     *
     * @mbggenerated Thu Jun 01 15:52:37 CST 2017
     */
    int updateByExample(@Param("record") AdminDepart record, @Param("example") AdminDepartExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_depart
     *
     * @mbggenerated Thu Jun 01 15:52:37 CST 2017
     */
    int updateByPrimaryKeySelective(AdminDepart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table admin_depart
     *
     * @mbggenerated Thu Jun 01 15:52:37 CST 2017
     */
    int updateByPrimaryKey(AdminDepart record);
}