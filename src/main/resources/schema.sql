CREATE TABLE IF NOT EXISTS users (
    PK_id UUID NOT NULL PRIMARY KEY,

    username NVARCHAR(100) NOT NULL,
    password NVARCHAR(100) NOT NULL,

    first_name NVARCHAR(50) NOT NULL,
    last_name NVARCHAR(50) NOT NULL,

    email_address NVARCHAR(50) NOT NULL,

-- FIXME    security_role NVARCHAR(50) NOT NULL CHECK(security_role IN ('ADMIN','USER')),
    is_account_non_expired BIT NOT NULL,
    is_account_non_locked BIT NOT NULL,
    is_credentials_non_expired BIT NOT NULL,
    is_enabled BIT NOT NULL
);

-------------------------------------------------------
CREATE TABLE IF NOT EXISTS files (
    PK_id UUID NOT NULL PRIMARY KEY,

    FK_owner_id UUID NOT NULL,
    FK_file_location_id UUID NOT NULL,

    user_provided_file_name NVARCHAR(120) NOT NULL,
    file_extension NVARCHAR(24) NOT NULL CHECK(file_extension IN ('PNG')),

    file_location NVARCHAR(240) NOT NULL
);

-------------------------------------------------------
CREATE TABLE IF NOT EXISTS receiptable_items_collections (
    PK_id UUID NOT NULL PRIMARY KEY,

    FK_owner_id UUID NOT NULL,

    name NVARCHAR(240) NOT NULL
);

CREATE TABLE IF NOT EXISTS receiptable_items (
    PK_id UUID NOT NULL PRIMARY KEY,

    FK_owner_id UUID NOT NULL,
    FK_receiptable_items_collections_id UUID NOT NULL,
    FK_image_file_id UUID NOT NULL,

    item_name NVARCHAR(240) NOT NULL,
    description NVARCHAR(480),
    invoice_datetime TIMESTAMP NOT NULL,
    monetary_amount_in_minor_units INTEGER NOT NULL,
    iso4217_currency_code NVARCHAR(3) NOT NULL
);

-------------------------------------------------------
-------------------------------------------------------
ALTER TABLE files
ADD FOREIGN KEY (FK_owner_id) REFERENCES users(PK_id);

-------------------------------------------------------
ALTER TABLE receiptable_items
ADD FOREIGN KEY (FK_owner_id) REFERENCES users(PK_id);

ALTER TABLE receiptable_items
ADD FOREIGN KEY (FK_receiptable_items_collections_id) REFERENCES receiptable_items_collections(PK_id);

ALTER TABLE receiptable_items
ADD FOREIGN KEY (FK_image_file_id) REFERENCES files(PK_id);