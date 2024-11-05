package com.klkk.CMD.fileSystem.entity;

import com.klkk.CMD.codeEditer.entity.Code;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "file_system")
public class FileSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private FileSystem parent;

    private String absolutePath;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType type;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "code_id", referencedColumnName = "code_id")
    private Code code;

    private Integer version = 1;

    public enum FileType {
        FOLDER,
        FILE
    }
    public boolean isDirectory() {
        return this.type == FileType.FOLDER;
    }

    public boolean isFile() {
        return this.type == FileType.FILE;
    }
}
