package net.zsoo.spring.mybatis;

import org.springframework.stereotype.Service;

@Service
public class MybatisServiceImpl implements MybatisService {

    private final RecordMapper recordMapper;

    public MybatisServiceImpl(RecordMapper recordMapper) {
        this.recordMapper = recordMapper;
    }

    @Override
    public Record[] getAllRecords() {
        return recordMapper.select();
    }

}
