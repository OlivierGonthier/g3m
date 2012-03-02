package com.roly.GiveMeMyMoney2.pret;

public class Objet extends AbstractPretable{

	private String name;
	private Pret pret;
	
	public Objet(){
		super(AbstractPretable.NATURE_OBJET);
	}
	
	public Objet(int id){
		super(AbstractPretable.NATURE_OBJET, id);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Pret getPret() {
		return pret;
	}

	public void setPret(Pret pret) {
		this.pret = pret;
	}

	@Override
	public Object getValeur() {
		return name;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getPret().getNom());
		builder.append(" : ");
		builder.append(getName());
		return builder.toString();
	}
}
