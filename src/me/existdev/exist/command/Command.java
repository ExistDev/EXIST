package me.existdev.exist.command;

public interface Command {
	
	boolean run(String args[]);
	
	String usage();

}
