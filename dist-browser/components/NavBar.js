/**
 * Left navigation rail - mirrors Android AppNavigationRail / NavigationTile
 */
import { AnsunaTheme } from '../theme';
export class NavBar {
    element;
    onScreenChange;
    currentScreen = 'home';
    constructor(onScreenChange) {
        this.onScreenChange = onScreenChange;
        this.element = this.createElement();
    }
    createElement() {
        const nav = document.createElement('nav');
        nav.id = 'sidebar';
        nav.style.width = `${AnsunaTheme.sidebarWidth}px`;
        nav.style.background = AnsunaTheme.colors.surface;
        nav.style.borderRight = `1px solid ${AnsunaTheme.colors.divider}`;
        nav.style.display = 'flex';
        nav.style.flexDirection = 'column';
        nav.style.padding = '16px 0';
        nav.style.flexShrink = '0';
        // Header
        const header = document.createElement('div');
        header.style.padding = '0 12px 24px 12px';
        const logo = document.createElement('div');
        logo.className = 'nav-logo';
        logo.textContent = 'Ansuna';
        logo.style.fontSize = '20px';
        logo.style.fontWeight = '700';
        logo.style.color = AnsunaTheme.colors.textOnSurface;
        logo.style.letterSpacing = '-0.5px';
        header.appendChild(logo);
        nav.appendChild(header);
        const items = document.createElement('div');
        items.className = 'nav-items';
        items.style.display = 'flex';
        items.style.flexDirection = 'column';
        items.style.gap = '8px';
        items.style.padding = '0 12px';
        const screenDefs = [
            { id: 'home', icon: '🏠', label: 'Home' },
            { id: 'search', icon: '🔍', label: 'Search' },
            { id: 'library', icon: '📚', label: 'Library' },
            { id: 'generate', icon: '✨', label: 'Generate' },
        ];
        screenDefs.forEach(({ id, icon, label }) => {
            const btn = document.createElement('button');
            btn.className = `nav-item ${id === this.currentScreen ? 'active' : ''}`;
            btn.dataset.screen = id;
            btn.style.display = 'flex';
            btn.style.flexDirection = 'column';
            btn.style.alignItems = 'center';
            btn.style.gap = '6px';
            btn.style.width = '48px';
            btn.style.height = '64px';
            btn.style.background = 'transparent';
            btn.style.border = 'none';
            btn.style.borderRadius = `${AnsunaTheme.radius}px`;
            btn.style.color = AnsunaTheme.colors.textMuted;
            btn.style.cursor = 'pointer';
            btn.style.fontSize = '11px';
            btn.style.lineHeight = '1.2';
            btn.style.textAlign = 'center';
            btn.style.padding = '8px 4px';
            const iconEl = document.createElement('span');
            iconEl.className = 'nav-icon';
            iconEl.textContent = icon;
            iconEl.style.fontSize = '20px';
            iconEl.style.lineHeight = '1';
            const labelEl = document.createElement('span');
            labelEl.className = 'nav-label';
            labelEl.textContent = label;
            labelEl.style.fontSize = '10px';
            labelEl.style.marginTop = '2px';
            btn.appendChild(iconEl);
            btn.appendChild(labelEl);
            btn.addEventListener('mouseenter', () => {
                if (!btn.classList.contains('active')) {
                    btn.style.background = 'rgba(255, 255, 255, 0.06)';
                    btn.style.color = AnsunaTheme.colors.textOnSurface;
                }
            });
            btn.addEventListener('mouseleave', () => {
                if (!btn.classList.contains('active')) {
                    btn.style.background = 'transparent';
                    btn.style.color = AnsunaTheme.colors.textMuted;
                }
            });
            btn.addEventListener('click', () => {
                this.setActive(id);
                this.onScreenChange(id);
            });
            items.appendChild(btn);
        });
        nav.appendChild(items);
        // Spacer
        const spacer = document.createElement('div');
        spacer.className = 'nav-spacer';
        spacer.style.flex = '1';
        nav.appendChild(spacer);
        // Settings at bottom
        const settings = document.createElement('div');
        settings.className = 'nav-items';
        settings.style.display = 'flex';
        settings.style.flexDirection = 'column';
        settings.style.gap = '8px';
        settings.style.padding = '0 12px';
        const settingsBtn = document.createElement('button');
        settingsBtn.className = 'nav-item';
        settingsBtn.dataset.screen = 'settings';
        settingsBtn.style.display = 'flex';
        settingsBtn.style.flexDirection = 'column';
        settingsBtn.style.alignItems = 'center';
        settingsBtn.style.gap = '6px';
        settingsBtn.style.width = '48px';
        settingsBtn.style.height = '64px';
        settingsBtn.style.background = 'transparent';
        settingsBtn.style.border = 'none';
        settingsBtn.style.borderRadius = `${AnsunaTheme.radius}px`;
        settingsBtn.style.color = AnsunaTheme.colors.textMuted;
        settingsBtn.style.cursor = 'pointer';
        settingsBtn.style.fontSize = '11px';
        settingsBtn.style.lineHeight = '1.2';
        settingsBtn.style.textAlign = 'center';
        settingsBtn.style.padding = '8px 4px';
        const sIcon = document.createElement('span');
        sIcon.className = 'nav-icon';
        sIcon.textContent = '⚙️';
        sIcon.style.fontSize = '20px';
        sIcon.style.lineHeight = '1';
        const sLabel = document.createElement('span');
        sLabel.className = 'nav-label';
        sLabel.textContent = 'Settings';
        sLabel.style.fontSize = '10px';
        sLabel.style.marginTop = '2px';
        settingsBtn.appendChild(sIcon);
        settingsBtn.appendChild(sLabel);
        settingsBtn.addEventListener('click', () => {
            this.setActive('settings');
            this.onScreenChange('settings');
        });
        settings.appendChild(settingsBtn);
        nav.appendChild(settings);
        return nav;
    }
    setActive(screenId) {
        this.currentScreen = screenId;
        const buttons = this.element.querySelectorAll('.nav-item');
        buttons.forEach((btn) => {
            const isActive = btn.dataset.screen === screenId;
            btn.classList.toggle('active', isActive);
            btn.style.background = isActive
                ? AnsunaTheme.colors.surfaceSelected
                : 'transparent';
            btn.style.color = isActive
                ? AnsunaTheme.colors.accent
                : AnsunaTheme.colors.textMuted;
        });
    }
    getElement() {
        return this.element;
    }
}
