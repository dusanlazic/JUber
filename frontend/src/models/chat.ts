export enum DriverStatus {
    ACTIVE="ACTIVE",
	INACTIVE="INACTIVE",
	OVERTIME="OVERTIME",
	DRIVING="DRIVING"
}

export interface ChatMessage {
    content: string,
    sentAt: Date,
}

export interface ChatMessageResponse extends ChatMessage{
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
    isResponded: boolean
}

export interface MsgFromSupportMessage extends ChatMessage{
}

export interface MsgFromUserMessage  extends ChatMessage {
    userId: string,
}

export interface NewConversationMessage {
    userId: string,
}