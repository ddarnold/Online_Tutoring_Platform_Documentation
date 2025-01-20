package de.thu.thutorium.api.transferObjects.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiverTO {
    private Long id;
    private String firstName;
    private String lastName;
}
