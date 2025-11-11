package com.student.Compass_Abroad.modal.notificaton

data class NotificationResponse(
    val statusCode: Int,
    val statusInfo: StatusInfo,
    val data: Data,
    val message: String,
    val success: Boolean
)

data class StatusInfo(
    val id: String,
    val statusCode: Int,
    val statusMessage: String,
    val description: String,
    val category: String
)

data class Data(
    val unReadNotificationCount: Int,
    val hasUnreadNotifications: Boolean,
    val recordsInfo: List<NotificationRecord>
)

data class NotificationRecord(
    val id: Int,
    val recipientInfo: RecipientInfo,
    val status: String,
    val deliveryStatus: String,
    val openStatus: String,
    val readStatus: String,
    val sentAt: String,
    val deliveredAt: String,
    val openedAt: String?,
    val readAt: String?,
    val createdAt: String,
    val updatedAt: String,
    val notificationInfo: NotificationInfo,
    val sentTemplateInfo: SentTemplateInfo
)

data class RecipientInfo(
    val roleInfo: RoleInfo,
    val userInfo: UserInfo
)

data class RoleInfo(
    val id: String,
    val name: String,
    val alias: String,
    val groupInfo: GroupInfo,
    val identifier: String
)

data class GroupInfo(
    val id: Int,
    val name: String,
    val type: String,
    val identifier: String
)

data class UserInfo(
    val id: Int,
    val agentInfo: AgentInfo,
    val lastName: String,
    val firstName: String,
    val identifier: String,
    val profilePictureUrl: String?
)

data class AgentInfo(
    val name: String,
    val identifier: String,
    val agentTypeInfo: AgentTypeInfo
)

data class AgentTypeInfo(
    val name: String,
    val identifier: String
)

data class NotificationInfo(
    val id: Int,
    val identifier: String,
    val title: String,
    val content: String,
    val variables: List<Variable>,
    val moduleId: Int,
    val moduleType: String,
    val moduleInfo: ModuleInfo,
    val status: String,
    val type: String
)

data class Variable(
    val name: String,
    val value: String
)

data class ModuleInfo(
    val id: Int,
    val mobile: String,
    val leadId: Int,
    val lastName: String,
    val firstName: String,
    val identifier: String,
    val countryCode: String,
    val programInfo: List<ProgramInfo>,
    val leadIdentifier: String,
    val institutionInfo: List<InstitutionInfo>
)

data class ProgramInfo(
    val id: Int,
    val isApplied: Int,
    val programData: ProgramData
)

data class ProgramData(
    val id: Int,
    val url: String,
    val name: String,
    val campus: String,
    val duration: Int,
    val identifier: String,
    val tuitionFee: String,
    val applicationFee: String
)

data class InstitutionInfo(
    val id: Int,
    val campusId: Int,
    val isApplied: Int,
    val institutionData: InstitutionData
)

data class InstitutionData(
    val url: String,
    val name: String,
    val campus: String,
    val country: String,
    val currency: String,
    val currencySymbol: String
)

data class SentTemplateInfo(
    val id: Int,
    val notificationTemplateInfo: NotificationTemplateInfo
)

data class NotificationTemplateInfo(
    val id: Int,
    val channelId: Int,
    val categoryId: Int,
    val isFallback: Int,
    val channelInfo: ChannelInfo,
    val categoryInfo: CategoryInfo
)

data class ChannelInfo(
    val id: Int,
    val identifier: String,
    val name: String,
    val type: String,
    val status: String
)

data class CategoryInfo(
    val id: Int,
    val identifier: String,
    val name: String,
    val triggerString: String,
    val status: String
)
