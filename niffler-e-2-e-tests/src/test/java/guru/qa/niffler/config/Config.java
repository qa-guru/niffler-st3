package guru.qa.niffler.config;

import org.apache.kafka.common.protocol.types.Field;

public interface Config {

	String databaseHost();

	default String databaseUser(){
		return "postgres";
	}

	default String databasePassword(){
		return "secret";
	}

	default String databaseUser(){
		return "postgres";
	}
}
