package io.openems.edge.meter.carlo.gavazzi.em24;

import io.openems.common.channel.PersistencePriority;
import io.openems.common.channel.Unit;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.component.OpenemsComponent;

public interface MeterCarloGavazziEm24 extends OpenemsComponent {

	public enum ChannelId implements io.openems.edge.common.channel.ChannelId {

		PHASE_L2_FREQ(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.HERTZ) //
				.persistencePriority(PersistencePriority.HIGH)), //
		;

		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		@Override
		public Doc doc() {
			return this.doc;
		}
	}

}
