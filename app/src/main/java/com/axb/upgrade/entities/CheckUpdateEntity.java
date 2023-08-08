package com.axb.upgrade.entities;

public class CheckUpdateEntity {
   private boolean fromUser;

   public CheckUpdateEntity(boolean fromUser) {
      this.fromUser = fromUser;
   }

   public boolean isFromUser() {
      return fromUser;
   }
   public void setFromUser(boolean fromUser) {
      this.fromUser = fromUser;
   }
}
