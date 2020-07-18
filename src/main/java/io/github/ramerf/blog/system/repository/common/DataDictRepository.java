package io.github.ramerf.blog.system.repository.common;

import java.util.List;
import io.github.ramerf.blog.system.entity.domain.common.DataDict;
import io.github.ramerf.blog.system.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DataDictRepository extends BaseRepository<DataDict, Long> {
  DataDict findByCodeAndIsDelete(String code, Boolean isDelete);

  DataDict findByDataDictTypeCodeAndCodeAndIsDelete(
      String dataDictTypeCode, String code, Boolean isDelete);

  @Query(
      "from io.github.ramerf.blog.system.entity.domain.common.DataDict dd where dd.dataDictType.code= :typeCode and dd.isDelete= :isDelete")
  List<DataDict> findByTypeCodeAndIsDelete(
      @Param("typeCode") String typeCode, @Param("isDelete") Boolean isDelete);

  @Query(
      "from io.github.ramerf.blog.system.entity.domain.common.DataDict dd where dd.dataDictType.code= :typeCode and dd.isDelete= :isDelete")
  Page<DataDict> findByTypeCodeAndIsDelete(
      @Param("typeCode") String typeCode, @Param("isDelete") Boolean isDelete, Pageable pageable);

  @Query(
      "from io.github.ramerf.blog.system.entity.domain.common.DataDict dd where dd.dataDictType.code= :typeCode and dd.isDelete= :isDelete and (dd.name like :criteria or dd.code like :criteria or dd.remark like :criteria )")
  Page<DataDict> findByTypeCodeAndIsDelete(
      @Param("typeCode") String typeCode,
      @Param("criteria") String criteria,
      @Param("isDelete") Boolean isDelete,
      Pageable pageable);

  List<DataDict> findByDataDictTypeCodeAndIsDelete(String typeCode, Boolean isDelete);
}
