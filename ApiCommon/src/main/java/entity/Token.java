package entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;
@Data
@AllArgsConstructor
public class Token {

   String uid ;
   String IpAddress;
   String userName;

}
