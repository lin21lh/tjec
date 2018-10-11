
package com.wfzcx.demo.dao;
/**
 * 
 */
 
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.demo.po.Demo;

@Scope("prototype")
@Repository
public class DemoDao extends GenericDao<Demo, Integer> {
}
