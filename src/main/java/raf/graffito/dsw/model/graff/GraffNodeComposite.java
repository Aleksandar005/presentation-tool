package raf.graffito.dsw.model.graff;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public abstract class GraffNodeComposite extends GraffNode{
    private List<GraffNode> children = new ArrayList<>();

    public GraffNodeComposite(GraffNode parent,String title) {
        super(parent,title);
    }

    public abstract void addChild(GraffNode child);

    public abstract void removeChild(GraffNode child);

    public GraffNode findByName(String name){
        if(this.getTitle().equals(name)){
            return this;
        }
        for(GraffNode g : children){
            GraffNode res = g.findByName(name);
            if(res != null){
                return res;
            }
        }
        return null;
    }
}
