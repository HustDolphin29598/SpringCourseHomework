# Hãy giải thích các annotation sau đây

- ```@Entity```: đánh dấu entity. Trường hợp không có name sẽ lấy name của class
- ```@Table```: bảng tương ứng trong database. Trường hợp không có name sẽ lấy name của class
- ```@Data```: thư viện của Lombok giúp sinh getter, setter cho class
- ```@NoArgsConstructor```: Tạo sẵn constuctor không chứa bất kỳ tham số nào
- ```@AllArgsConstructor```: Tạo sẵn constuctor nhận tất cả các attributes của class làm tham số
- ```@Id```: Đánh dấu 1 attribute làm Id
- ```@GeneratedValue```: Tự sinh ra giá trị
- ```@Column```: Config cho 1 attribute của class là 1 column của bảng tương ứng trong database
- ```@NaturalId```: Đánh dấu 1 field làm naturalKey - bên cạnh surrogate key, key này mang ý nghĩa logic, vd như email của 1 user luôn là unique
- ```@OneToMany```: Đánh dấu quan hệ OneToMany của class hiện tại với 1 class khác
- ```cascade = CascadeType.ALL``` : sử dụng tất cả các loại Cascade(DETACH, MERGE, PERSIST, REFRESH, REMOVE)
- ```orphanRemoval = true``` các phần tử con sẽ bị xóa khi nó bị xóa khỏi collection của phần tử cha
- ```fetch = FetchType.LAZY```: khi select đối tượng (class) ở trong db thì sẽ không lấy các đối tượng liên quan. giá trị của những đối tượng liên quan sẽ được query khi gọi đến nó trong code
- ```fetch = FetchType.EAGER```: khi select đối tượng (class) ở trong db thì sẽ lấy ra hết các đối tượng liên quan và lưu vào biến tương ứng
- ```@JoinColumn(name = "user_id")```:  field liên kết tương ứng ở bảng còn lại trong quan hệ
- ```@PrePersist```: Thực thi trước khi entity được persist (được lưu vào database) bởi method persist()
- ```@PreUpdate```: Thực thi trước khi entity được update.
- ```@ManyToOne``` Đánh dấu quan hệ ManyToOne của class hiện tại với 1 class khác
- ```@JoinTable```
- Giải thích
  ```java
  @JoinTable(
    name = "post_tag",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  ```
  Quan hệ Many-To-Many: name là bảng mapping giữa Tag và Post, joinColumn là field liên kết tương ứng ở bảng mapping
  inverseJoinColumns là field sử dụng để liên kết với bảng còn lại trong quan hệ
- ```@FullTextField```: Đánh dấu cho Full Text Index
