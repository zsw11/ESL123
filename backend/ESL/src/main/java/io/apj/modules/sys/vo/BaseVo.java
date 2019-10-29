package io.apj.modules.sys.vo;

import java.util.List;

public interface BaseVo<V, E> {
	
	List<V> makeVoList(List<?> elist);
}
