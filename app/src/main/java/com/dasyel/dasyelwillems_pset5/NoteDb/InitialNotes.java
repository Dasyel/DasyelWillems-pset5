package com.dasyel.dasyelwillems_pset5.NoteDb;

public final class InitialNotes {
    private static final String floppyEmoji = new String(Character.toChars(0x1F4BE));
    private static final String noteEmoji = new String(Character.toChars(0x1F4C3));
    private static final String openEmoji = new String(Character.toChars(0x1F4C2));
    private static final String fireEmoji = new String(Character.toChars(0x1F525));
    private static final String plusEmoji = new String(Character.toChars(0x2795));
    private static final String handEmoji = new String(Character.toChars(0x270D));
    public static final String[][] initMessages = {
            {openEmoji+" Open Note", "touch a note to open it"},
            {noteEmoji+" Add Note", "press the" + plusEmoji + "button at the top right"},
            {floppyEmoji + " Save Note", "press the "+ floppyEmoji +
                    " button at the top right or just press back"},
            {fireEmoji+" Delete Note/List", "Long press a list in the side-menu to delete it. " +
                    "Note that the Default list can not be removed" +
                    "View a note and use the delete button to delete it."},
            {handEmoji + " Set note as done", "Long press a note in the list to strike it through."}
    };
}
