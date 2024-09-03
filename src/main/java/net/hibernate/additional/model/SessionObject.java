package net.hibernate.additional.model;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class SessionObject
{
    private String name;
    private String sessionId;
    private String password;
}