package com.thoughtworks.rslist.po;


import com.sun.javafx.beans.IDProperty;
import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
@Entity
@Data
@Table(name = "rsEvent")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class RsEventPo {
    @Id
    @GeneratedValue
    private int id;
    private String eventName;
    private String keyWord;

    @ManyToOne
    private UserPo UserPo;


}
