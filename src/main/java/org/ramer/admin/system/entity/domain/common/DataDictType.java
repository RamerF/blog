package org.ramer.admin.system.entity.domain.common;

import org.ramer.admin.system.entity.domain.AbstractEntity;
import lombok.*;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = DataDictType.TABLE_NAME)
@Table(appliesTo = DataDictType.TABLE_NAME, comment = "数据字典类别")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataDictType extends AbstractEntity {
    public static final String TABLE_NAME = "data_dict_type";


    @Column(
            nullable = false,
            unique = true,
            length = 25,
            columnDefinition = "VARCHAR(25) NOT NULL UNIQUE")
    private String code;

    @Column(nullable = false, length = 25, columnDefinition = "VARCHAR(25) NOT NULL UNIQUE")
    private String name;

    @Column(length = 100, columnDefinition = "VARCHAR(100)")
    private String remark;

    public static final String TYPE = "QUESTION_TYPE";

    public enum Type {
        QUESTION_TYPE("QUESTION_TYPE", "问题类型"),
        ;
        private String desc;
        private String code;

        Type(String code, String desc) {
            this.desc = desc;
            this.code = code;
        }

        @Override
        public String toString() {
            return desc;
        }

        public String code() {
            return code;
        }
    }
}
