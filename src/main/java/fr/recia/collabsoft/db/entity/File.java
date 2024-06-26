/*
 * Copyright (C) 2023 GIP-RECIA, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.recia.collabsoft.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class File implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  @ToString.Include
  private Long id;

  @Column(name = "uuid", nullable = false)
  @ToString.Include
  private String uuid;

  @Column(name = "title", nullable = false)
  @ToString.Include
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "data", nullable = false)
  private String data;

  @ManyToOne
  @JoinColumn(name = "creator_id", nullable = false)
  @ToString.Include
  private User creator;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "creation_date", nullable = false)
  @ToString.Include
  private Date creationDate;

  @ManyToOne
  @JoinColumn(name = "last_editor_id", nullable = false)
  @ToString.Include
  private User lastEditor;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "edition_date", nullable = false)
  @ToString.Include
  private Date editionDate;

  @ManyToOne
  @JoinColumn(name = "associated_app_id")
  @ToString.Include
  private AssociatedApp associatedApp;

  @Column(name = "public", nullable = false)
  @ToString.Include
  private Boolean pub;

  @PrePersist
  public void prePersist() {
    Date d = new Date();
    d.setTime(Calendar.getInstance().getTimeInMillis());
    if (this.creationDate == null) this.creationDate = d;
    if (this.editionDate == null) this.editionDate = d;
  }

  @PreUpdate
  public void preUpdate() {
    this.editionDate.setTime(Calendar.getInstance().getTimeInMillis());
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    File file = (File) o;
    return getId() != null && Objects.equals(getId(), file.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }

}
