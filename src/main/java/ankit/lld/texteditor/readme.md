Role	        Our Case
Command	    Interface for execute() and undo()
Concrete    Commands	InsertCommand, UpdateCommand, DeleteCommand
Receiver	TextEditor (actual text storage & modification)
Invoker	    EditorInvoker (runs commands, keeps history)
Client	    Main method (creates commands and passes to invoker)