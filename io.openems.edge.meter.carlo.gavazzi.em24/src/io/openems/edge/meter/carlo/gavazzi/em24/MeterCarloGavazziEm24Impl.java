package io.openems.edge.meter.carlo.gavazzi.em24;

import static io.openems.edge.bridge.modbus.api.ElementToChannelConverter.SCALE_FACTOR_MINUS_1;
import static io.openems.edge.bridge.modbus.api.ElementToChannelConverter.SCALE_FACTOR_MINUS_1_AND_INVERT_IF_TRUE;
import static io.openems.edge.bridge.modbus.api.ElementToChannelConverter.DIRECT_1_TO_1;
import static io.openems.edge.bridge.modbus.api.ElementToChannelConverter.SCALE_FACTOR_2;

import org.osgi.service.cm.ConfigurationAdmin;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.metatype.annotations.Designate;

import io.openems.common.exceptions.OpenemsException;
import io.openems.common.types.MeterType;
import io.openems.edge.bridge.modbus.api.AbstractOpenemsModbusComponent;
import io.openems.edge.bridge.modbus.api.BridgeModbus;
import io.openems.edge.bridge.modbus.api.ModbusComponent;
import io.openems.edge.bridge.modbus.api.ModbusProtocol;
import io.openems.edge.bridge.modbus.api.element.UnsignedWordElement;
import io.openems.edge.bridge.modbus.api.element.DummyRegisterElement;
import io.openems.edge.bridge.modbus.api.element.SignedDoublewordElement;
import io.openems.edge.bridge.modbus.api.element.WordOrder;
import io.openems.edge.bridge.modbus.api.task.FC3ReadRegistersTask;
import io.openems.edge.bridge.modbus.api.task.FC4ReadInputRegistersTask;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.taskmanager.Priority;
import io.openems.edge.meter.api.ElectricityMeter;


@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "io.openems.edge.meter.carlo.gavazzi.em24", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE //
)
public class MeterCarloGavazziEm24Impl extends AbstractOpenemsModbusComponent
		implements MeterCarloGavazziEm24, ElectricityMeter, ModbusComponent, OpenemsComponent {

	@Reference
	private ConfigurationAdmin cm;

	@Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MANDATORY)
	protected void setModbus(BridgeModbus modbus) {
		super.setModbus(modbus);
	}

	private Config config = null;

	public MeterCarloGavazziEm24Impl() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				ModbusComponent.ChannelId.values(), //
				ElectricityMeter.ChannelId.values(), //
				MeterCarloGavazziEm24.ChannelId.values() //
		);
	}

	@Activate
	private void activate(ComponentContext context, Config config) throws OpenemsException {
		if (super.activate(context, config.id(), config.alias(), config.enabled(), config.modbusUnitId(), this.cm,
				"Modbus", config.modbus_id())) {
			return;
		}
		this.config = config;
	}

	@Override
	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	@Override
	protected ModbusProtocol defineModbusProtocol() {
		final var offset = 300000 + 1;

		return new ModbusProtocol(this, //
				//new FC4ReadInputRegistersTask(000, Priority.HIGH, //
						//m(ElectricityMeter.ChannelId.ACTIVE_POWER, new UnsignedWordElement(000)), //
						//m(ElectricityMeter.ChannelId.REACTIVE_POWER, new UnsignedWordElement(101)), //
						//m(MeterCarloGavazziEm24.ChannelId.PHASE_L2_FREQ, new UnsignedWordElement(102))//
				// new DummyRegisterElement(102),//
				//), //

				
						
				new FC3ReadRegistersTask(300013 - offset, Priority.HIGH, 
						m(ElectricityMeter.ChannelId.CURRENT_L1, new SignedDoublewordElement(300013 - offset).wordOrder(WordOrder.LSWMSW), DIRECT_1_TO_1),
						m(ElectricityMeter.ChannelId.CURRENT_L2, new SignedDoublewordElement(300015 - offset).wordOrder(WordOrder.LSWMSW), DIRECT_1_TO_1),
						m(ElectricityMeter.ChannelId.CURRENT_L3, new SignedDoublewordElement(300017 - offset).wordOrder(WordOrder.LSWMSW), DIRECT_1_TO_1),
						m(ElectricityMeter.ChannelId.ACTIVE_POWER_L1, new SignedDoublewordElement(300019 - offset).wordOrder(WordOrder.LSWMSW), SCALE_FACTOR_MINUS_1),
						m(ElectricityMeter.ChannelId.ACTIVE_POWER_L2, new SignedDoublewordElement(300021 - offset).wordOrder(WordOrder.LSWMSW), SCALE_FACTOR_MINUS_1),
						m(ElectricityMeter.ChannelId.ACTIVE_POWER_L3, new SignedDoublewordElement(300023 - offset).wordOrder(WordOrder.LSWMSW), SCALE_FACTOR_MINUS_1),
						new DummyRegisterElement(300025 - offset),
						new DummyRegisterElement(300027 - offset),
						new DummyRegisterElement(300029 - offset),
						m(ElectricityMeter.ChannelId.REACTIVE_POWER_L1, new SignedDoublewordElement(300031 - offset).wordOrder(WordOrder.LSWMSW), SCALE_FACTOR_MINUS_1_AND_INVERT_IF_TRUE(this.config.invert())),
						m(ElectricityMeter.ChannelId.REACTIVE_POWER_L2, new SignedDoublewordElement(300033 - offset).wordOrder(WordOrder.LSWMSW), SCALE_FACTOR_MINUS_1_AND_INVERT_IF_TRUE(this.config.invert())),
						m(ElectricityMeter.ChannelId.REACTIVE_POWER_L3, new SignedDoublewordElement(300035 - offset).wordOrder(WordOrder.LSWMSW), SCALE_FACTOR_MINUS_1_AND_INVERT_IF_TRUE(this.config.invert()))),

						
				new FC3ReadRegistersTask(300001 - offset, Priority.LOW, 
						m(ElectricityMeter.ChannelId.VOLTAGE_L1, new SignedDoublewordElement(300001 - offset).wordOrder(WordOrder.LSWMSW), SCALE_FACTOR_2),
						m(ElectricityMeter.ChannelId.VOLTAGE_L2, new SignedDoublewordElement(300003 - offset).wordOrder(WordOrder.LSWMSW), SCALE_FACTOR_2),
						m(ElectricityMeter.ChannelId.VOLTAGE_L3, new SignedDoublewordElement(300005 - offset).wordOrder(WordOrder.LSWMSW), SCALE_FACTOR_2)),
						

				new FC3ReadRegistersTask(300041 - offset, Priority.LOW, 
						m(ElectricityMeter.ChannelId.ACTIVE_POWER, new SignedDoublewordElement(300041 - offset).wordOrder(WordOrder.LSWMSW), SCALE_FACTOR_MINUS_1_AND_INVERT_IF_TRUE(this.config.invert()))));
						
					
	}

	@Override
	public String debugLog() {
		return "L:" + this.getActivePower().asString();
	}

	@Override
	public MeterType getMeterType() {
		return this.config.type();
	}
}
