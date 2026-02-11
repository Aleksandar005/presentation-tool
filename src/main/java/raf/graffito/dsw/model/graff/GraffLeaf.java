package raf.graffito.dsw.model.graff;

public abstract class GraffLeaf extends GraffNode{
    public GraffLeaf(GraffNode parent,String title) {
        super(parent,title);
    }

    public GraffNode findByName(String name){
        if(this.getTitle().equals(name)){
            return this;
        }
        return null;
    }
}
