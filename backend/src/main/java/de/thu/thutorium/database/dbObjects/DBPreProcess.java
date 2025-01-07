package de.thu.thutorium.database.dbObjects;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.lang.reflect.Field;

public class DBPreProcess {
  @PrePersist
  @PreUpdate
  public void preprocessFields(Object entity) {
    Field[] fields = entity.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.getType().equals(String.class)) {
        field.setAccessible(true);
        try {
          String value = (String) field.get(entity);
          if (value != null) {
            field.set(entity, value.trim().toLowerCase());
          }
        } catch (IllegalAccessException e) {
          throw new RuntimeException("Failed to process field: " + field.getName(), e);
        }
      }
    }
  }
}
