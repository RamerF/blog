package ${basePath}${moduleName}.repository${subDir};

import ${basePath}${moduleName}.entity.domain${subDir}.${name};
import ${basePath}.system.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${name}Repository extends BaseRepository<${name}, Long> {}
