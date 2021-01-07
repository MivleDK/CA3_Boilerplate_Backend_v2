
package dto;

import entities.Hobby;


public class HobbyDTO {
    
    private String description;
    private String name;

    public HobbyDTO(Hobby hobby) {
        this.description = hobby.getDescription();
        this.name = hobby.getName();
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
