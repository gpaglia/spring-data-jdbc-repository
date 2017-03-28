package cz.jirutka.spring.data.jdbc.ext;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;

public interface PersistableObject<ID extends Serializable> extends Persistable<ID> {
	Long getVersion();
	
	default boolean isNew() {
		return getVersion() != null;
	}
}
