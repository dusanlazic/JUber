export interface ChatMessage {
    content: string,
    sentAt: Date,
    isFromSupport: boolean
}

export interface ChatMessageRequest {
    content: string; //max 500
}
export interface ChatConversationResponse {
    messagePreview: string,
    date: Date,
    userId: string,
    userFullName: string
    userImageUrl: string,
    isRead?: boolean;
    isSelected?: boolean;
    conversationId: string;
    isSeen: boolean;
}

export interface MsgFromUserMessage  extends ChatMessage {
    userId: string,
}