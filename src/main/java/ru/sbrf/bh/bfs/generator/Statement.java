package ru.sbrf.bh.bfs.generator;

/**
 * Created by sbt-barsukov-sv on 28.05.2018.
 */
public interface Statement {
    String LOGGER_INFO_LEVEL = "LOGGER.info($S)";
    String LOGGER_DEBUG_LEVEL = "LOGGER.debug($S)";
    String LOGGER_ERROR_LEVEL = "LOGGER.error($S,e)";

    String EXCEPTION_THROW = "throw e";

    String INITIALIZE_SUCCESS_FLAG = "boolean success = false";
    String CHANGE_SUCCESS_FLAG = "success = true";

    String INITIALIZE_RESPONSE = "$T rs = null";
    String RETURN_RESPONSE = "return rs";

    String INITIALIZE_REQUEST = "$T rq = null";
}
