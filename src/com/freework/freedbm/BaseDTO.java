package com.freework.freedbm;

import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;

public interface BaseDTO  extends DTO{
	JdbcForDTO[] updateField();
	TableDataManager managerCfg();
}
