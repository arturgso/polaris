package io.vexis.polaris.domain.models.entities;

import java.util.ArrayList;
import java.util.List;

import io.vexis.polaris.shared.abstracts.ListEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tab_gift_list")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GiftList extends ListEntity {

    @Builder.Default
    @OneToMany(mappedBy = "giftList", fetch = FetchType.LAZY)
    private List<Gift> gifts = new ArrayList<>();

}