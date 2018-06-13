package ru.sbrf.bh.bfs.ufs.type.poet;

import ru.sbrf.bh.bfs.generator.type.api.ApiTypePoet;
import ru.sbrf.bh.bfs.generator.type.service.ServiceTypePoet;

import java.util.*;

/**
 * Created by sbt-barsukov-sv on 29.05.2018.
 */
public class PoetFactory {

    //Генераторы классов под каждый API
    private static class ApiPoetListRepo {
        private final static List<ApiTypePoet> poetList = Collections.unmodifiableList(new ArrayList<ApiTypePoet>(Arrays.asList(new DaPoet(), new FgPoet())));
    }


    public static List<ApiTypePoet> getApiPoetList() {
        return ApiPoetListRepo.poetList;
    }

    //Генерация сервисных классов (1 на jar)
    private static class ServicePoetListRepo {
        private final static Map<String,ServiceTypePoet> poetMap;
        static {
            HashMap<String,ServiceTypePoet> map = new HashMap<>();
            map.put("bfsMonitoringServiceImpl",new BfsMonitoringPoetImpl());
            map.put("bfsMonitoringService", new BfsMonitoringPoet());
            poetMap = Collections.unmodifiableMap(map);
        }
    }
    public static Map<String,ServiceTypePoet> getServicePoetMap() {
        return ServicePoetListRepo.poetMap;
    }


}
