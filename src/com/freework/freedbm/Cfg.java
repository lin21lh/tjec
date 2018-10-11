package com.freework.freedbm;
import com.freework.freedbm.cfg.dbsqltype.BuilderSQL;
import com.freework.freedbm.cfg.dbsqltype.OracleBuilderSQL;
import com.freework.freedbm.cfg.fieldcfg.type.BigDecimalType;
import com.freework.freedbm.cfg.fieldcfg.type.BooleanType;
import com.freework.freedbm.cfg.fieldcfg.type.ByteType;
import com.freework.freedbm.cfg.fieldcfg.type.DateType;
import com.freework.freedbm.cfg.fieldcfg.type.DoubleType;
import com.freework.freedbm.cfg.fieldcfg.type.IntegerType;
import com.freework.freedbm.cfg.fieldcfg.type.LongType;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.cfg.fieldcfg.type.ShortType;
import com.freework.freedbm.cfg.fieldcfg.type.StringType;
import com.freework.freedbm.cfg.fieldcfg.type.TimestampType;
public interface Cfg {
	public static final BuilderSQL DB_TYPE=new BuilderSQLProxy();
	public static final SQLType Short = new ShortType();
	public static final SQLType String = new StringType();
	public static final SQLType Integer = new IntegerType();
	public static final SQLType Long = new LongType();
	public static final SQLType Double = new DoubleType();
	public static final SQLType BigDecimal = new BigDecimalType();
	public static final SQLType Date = new DateType();
	public static final SQLType Timestamp = new TimestampType();
	public static final SQLType Byte = new ByteType();
	public static final SQLType Boolean = new BooleanType();

}
