package com.ourdressingtable.dressingtable.domain;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.ourdressingtable.common.util.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "dressing_tables")
//@SQLDelete(sql = "UPDATE dressing_tables SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class DressingTable extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dressing_table_id")
    private Long id;

    private String name;

    private String imageUrl;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy ="dressingTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberCosmetic> memberCosmetics = new ArrayList<>();

    @Builder
    public DressingTable(Long id, String name, String imageUrl, Member member, boolean isDeleted, LocalDateTime deletedAt) {
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
        this.deletedAt = LocalDateTime.now();
    }

    public void addMemberCosmetic(MemberCosmetic memberCosmetic) {
        if(this.isDeleted) {
            throw new OurDressingTableException(ErrorCode.DELETED_DRESSING_TABLE);
        }

        if(memberCosmetic.getMember() == null || !memberCosmetic.getMember().getId().equals(this.member.getId())) {
            throw new OurDressingTableException(ErrorCode
                    .NO_PERMISSION_FOR_DRESSING_TABLE);
        }

        memberCosmetics.add(memberCosmetic);
        memberCosmetic.changeDressingTable(this);

    }
}
