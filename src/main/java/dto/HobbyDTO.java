
package dto;

import entities.Hobby;


public class HobbyDTO {
    
    private String description;
    private String name;
    private Long id;

    public HobbyDTO() {
    }

    public HobbyDTO(Hobby hobby) {
        this.description = hobby.getDescription();
        this.name = hobby.getName();
        this.id = hobby.getId();
    }

    public HobbyDTO(String desc, String Hname) {
        this.description = desc;
        this.name = Hname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

}
