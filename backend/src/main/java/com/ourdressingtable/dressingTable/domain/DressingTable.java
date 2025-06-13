package com.ourdressingtable.dressingTable.domain;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.ourdressingtable.common.util.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "dressing_tables")
//@SQLDelete(sql = "UPDATE dressing_tables SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class DressingTable extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public DressingTable(Long id, String name, String imageUrl, Member member, boolean isDeleted, Timestamp deletedAt) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.member = member;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }

    public void updateName(String newName) {
        this.name = newName;
    }
    public void updateImageUrl(String newImageUrl) {
        this.imageUrl = newImageUrl;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
        this.deletedAt = new Timestamp(System.currentTimeMillis());
    }
}
