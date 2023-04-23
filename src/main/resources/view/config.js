import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { ToggleModule } from './toggle-module/ToggleModule.js'
import { EndScreenModule } from './endscreen-module/EndScreenModule.js';
import { TooltipModule } from './tooltip-module/TooltipModule.js';

export const gameName = 'Animal Chess';
export const playerColors = ['#d0332d', '#0e36ff'];

// List of viewer modules that you want to use in your game
export const modules = [
	GraphicEntityModule,
	ToggleModule,
    TooltipModule,
	EndScreenModule
];

// The list of toggles displayed in the options of the viewer
export const options = [
    ToggleModule.defineToggle({
        toggle: 'debugToggle',
        title: 'DEBUG',
        values: {
            'ON': true,
            'OFF': false
        },
        default: true
    })
];