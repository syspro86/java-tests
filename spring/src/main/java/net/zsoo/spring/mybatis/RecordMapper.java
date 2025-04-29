package net.zsoo.spring.mybatis;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordMapper {
    Record[] select();
}
