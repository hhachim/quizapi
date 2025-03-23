-- V1__init_schema.sql
-- Création du schéma initial pour le Core

-- Extension pour les UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Table des plugins
CREATE TABLE plugins (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    version TEXT NOT NULL,
    description TEXT,
    enabled BOOLEAN NOT NULL DEFAULT false,
    config JSONB DEFAULT '{}'::jsonb,
    installed_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    last_enabled_at TIMESTAMPTZ,
    last_disabled_at TIMESTAMPTZ
);

-- Index pour accélérer la recherche des plugins activés
CREATE INDEX idx_plugins_enabled ON plugins(enabled) WHERE enabled = true;

-- Commentaires sur les tables et colonnes
COMMENT ON TABLE plugins IS 'Plugins installés dans le système';
COMMENT ON COLUMN plugins.id IS 'Identifiant unique du plugin';
COMMENT ON COLUMN plugins.name IS 'Nom lisible du plugin';
COMMENT ON COLUMN plugins.version IS 'Version du plugin';
COMMENT ON COLUMN plugins.description IS 'Description des fonctionnalités du plugin';
COMMENT ON COLUMN plugins.enabled IS 'État d''activation du plugin';
COMMENT ON COLUMN plugins.config IS 'Configuration au format JSON du plugin';
COMMENT ON COLUMN plugins.installed_at IS 'Date d''installation du plugin';
COMMENT ON COLUMN plugins.last_enabled_at IS 'Dernière date d''activation du plugin';
COMMENT ON COLUMN plugins.last_disabled_at IS 'Dernière date de désactivation du plugin';