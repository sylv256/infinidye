package gay.sylv.infinidye.attachment;

import static gay.sylv.infinidye.Infinidye.modId;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public final class ModAttachments {
	public static final AttachmentType<InfinidyeAttachment> INFINIDYE = AttachmentRegistry.create(
			modId("infinidye"),
			builder -> builder
					.persistent(InfinidyeAttachment.CODEC)
					.syncWith(
							InfinidyeAttachment.STREAM_CODEC,
							AttachmentSyncPredicate.all()
					)
	);

	private ModAttachments() {
	}

	public static void initialize() {
	}
}
