package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.annotations.BelongsTo;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Order type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Orders", authRules = {
  @AuthRule(allow = AuthStrategy.GROUPS, groupClaim = "cognito:groups", groups = { "buyers" }, operations = { ModelOperation.READ, ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE }),
  @AuthRule(allow = AuthStrategy.GROUPS, groupClaim = "cognito:groups", groups = { "sellers" }, operations = { ModelOperation.READ })
})
public final class Order implements Model {
  public static final QueryField ID = field("id");
  public static final QueryField QUANTITY = field("quantity");
  public static final QueryField CREATED_AT = field("createdAt");
  public static final QueryField UPDATED_AT = field("updatedAt");
  public static final QueryField INVENTORY = field("orderInventoryId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Float") Float quantity;
  private final @ModelField(targetType="AWSDateTime") Temporal.DateTime createdAt;
  private final @ModelField(targetType="AWSDateTime") Temporal.DateTime updatedAt;
  private final @ModelField(targetType="Inventory") @BelongsTo(targetName = "orderInventoryId", type = Inventory.class) Inventory Inventory;
  public String getId() {
      return id;
  }
  
  public Float getQuantity() {
      return quantity;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  public Inventory getInventory() {
      return Inventory;
  }
  
  private Order(String id, Float quantity, Temporal.DateTime createdAt, Temporal.DateTime updatedAt, Inventory Inventory) {
    this.id = id;
    this.quantity = quantity;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.Inventory = Inventory;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Order order = (Order) obj;
      return ObjectsCompat.equals(getId(), order.getId()) &&
              ObjectsCompat.equals(getQuantity(), order.getQuantity()) &&
              ObjectsCompat.equals(getCreatedAt(), order.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), order.getUpdatedAt()) &&
              ObjectsCompat.equals(getInventory(), order.getInventory());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getQuantity())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .append(getInventory())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Order {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("quantity=" + String.valueOf(getQuantity()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()) + ", ")
      .append("Inventory=" + String.valueOf(getInventory()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static Order justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Order(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      quantity,
      createdAt,
      updatedAt,
      Inventory);
  }
  public interface BuildStep {
    Order build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep quantity(Float quantity);
    BuildStep createdAt(Temporal.DateTime createdAt);
    BuildStep updatedAt(Temporal.DateTime updatedAt);
    BuildStep inventory(Inventory inventory);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private Float quantity;
    private Temporal.DateTime createdAt;
    private Temporal.DateTime updatedAt;
    private Inventory Inventory;
    @Override
     public Order build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Order(
          id,
          quantity,
          createdAt,
          updatedAt,
          Inventory);
    }
    
    @Override
     public BuildStep quantity(Float quantity) {
        this.quantity = quantity;
        return this;
    }
    
    @Override
     public BuildStep createdAt(Temporal.DateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    @Override
     public BuildStep updatedAt(Temporal.DateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    
    @Override
     public BuildStep inventory(Inventory inventory) {
        this.Inventory = inventory;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, Float quantity, Temporal.DateTime createdAt, Temporal.DateTime updatedAt, Inventory inventory) {
      super.id(id);
      super.quantity(quantity)
        .createdAt(createdAt)
        .updatedAt(updatedAt)
        .inventory(inventory);
    }
    
    @Override
     public CopyOfBuilder quantity(Float quantity) {
      return (CopyOfBuilder) super.quantity(quantity);
    }
    
    @Override
     public CopyOfBuilder createdAt(Temporal.DateTime createdAt) {
      return (CopyOfBuilder) super.createdAt(createdAt);
    }
    
    @Override
     public CopyOfBuilder updatedAt(Temporal.DateTime updatedAt) {
      return (CopyOfBuilder) super.updatedAt(updatedAt);
    }
    
    @Override
     public CopyOfBuilder inventory(Inventory inventory) {
      return (CopyOfBuilder) super.inventory(inventory);
    }
  }
  
}
